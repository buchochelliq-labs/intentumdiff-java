package dev.intentdiff;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

/**
 * The thin Java binding for the IntentDiff engine.
 *
 * <p>The engine (buchochelliq-labs/intentdiff-core) ships a native shared library exposing one
 * stable C ABI: {@code intentdiff_call(name, args_json) -> heap JSON envelope} freed via
 * {@code intentdiff_free}. This binding does zero functional work — it marshals arguments to
 * JSON, calls the engine via java.lang.foreign, and surfaces the {ok, result, error, error_type}
 * envelope, exactly like the Python ctypes binding.
 *
 * <p>Envelope parsing here is deliberately minimal (no JSON dependency in the scaffold): callers
 * receive the raw envelope JSON via {@link #callRaw}; a typed layer with a JSON library lands as
 * the binding grows.
 */
public final class IntentDiff implements AutoCloseable {
    private final Arena arena;
    private final MethodHandle call;
    private final MethodHandle free;

    private IntentDiff(Arena arena, MethodHandle call, MethodHandle free) {
        this.arena = arena;
        this.call = call;
        this.free = free;
    }

    /** Loads the engine shared library (intentdiff_rust_core.{dll,so,dylib}). */
    public static IntentDiff load(Path library) {
        Arena arena = Arena.ofShared();
        SymbolLookup lookup = SymbolLookup.libraryLookup(library, arena);
        Linker linker = Linker.nativeLinker();
        MethodHandle call = linker.downcallHandle(
                lookup.find("intentdiff_call").orElseThrow(
                        () -> new IllegalStateException("intentdiff_call not exported")),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        MethodHandle free = linker.downcallHandle(
                lookup.find("intentdiff_free").orElseThrow(
                        () -> new IllegalStateException("intentdiff_free not exported")),
                FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
        return new IntentDiff(arena, call, free);
    }

    /**
     * Invokes an engine function with a raw JSON argument array and returns the raw
     * {@code {ok, result, error, error_type}} envelope JSON.
     */
    public String callRaw(String name, String argsJsonArray) {
        try (Arena callArena = Arena.ofConfined()) {
            MemorySegment cName = callArena.allocateUtf8String(name);
            MemorySegment cArgs = callArena.allocateUtf8String(argsJsonArray);
            MemorySegment ret = (MemorySegment) call.invoke(cName, cArgs);
            if (ret.equals(MemorySegment.NULL)) {
                throw new IllegalStateException("intentdiff_call returned NULL");
            }
            MemorySegment sized = ret.reinterpret(Long.MAX_VALUE);
            String envelope = sized.getUtf8String(0);
            free.invoke(ret);
            return envelope;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new IllegalStateException("intentdiff_call failed", t);
        }
    }

    @Override
    public void close() {
        arena.close();
    }
}
