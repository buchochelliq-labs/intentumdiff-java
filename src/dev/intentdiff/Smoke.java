package dev.intentdiff;

import java.nio.file.Path;

/**
 * Scaffold smoke: {@code java --enable-preview -cp out dev.intentdiff.Smoke <path-to-core-lib>}
 * (or set INTENTDIFF_CORE_LIB). Prints the version + supports_language envelopes and exits
 * non-zero on any failure.
 */
public final class Smoke {
    public static void main(String[] args) {
        String lib = args.length > 0 ? args[0] : System.getenv("INTENTDIFF_CORE_LIB");
        if (lib == null || lib.isBlank()) {
            System.err.println("usage: Smoke <path-to-intentdiff_rust_core library>");
            System.exit(2);
        }
        try (IntentDiff engine = IntentDiff.load(Path.of(lib))) {
            String version = engine.callRaw("version", "[]");
            System.out.println("version envelope: " + version);
            if (!version.contains("\"ok\":true")) {
                throw new IllegalStateException("version call failed: " + version);
            }
            String supports = engine.callRaw("supports_language", "[\"python\"]");
            System.out.println("supports_language envelope: " + supports);
            if (!supports.contains("\"ok\":true") || !supports.contains("true")) {
                throw new IllegalStateException("supports_language failed: " + supports);
            }
            // A MISSING required argument is the canonical value_error (bad_request is reserved for malformed args_json itself) (extra args on a
            // zero-arg call are ignored by the ABI's positional arg readers).
            String bad = engine.callRaw("supports_language", "[]");
            System.out.println("bad-args envelope: " + bad);
            if (!bad.contains("value_error")) {
                throw new IllegalStateException("expected value_error envelope: " + bad);
            }
            System.out.println("SMOKE OK");
        }
    }
}
