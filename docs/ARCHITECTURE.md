# intentdiff-java architecture — the thin Java binding

Zero functional work, built on java.lang.foreign (FFM): downcall handles to the engine's
[C ABI](https://github.com/buchochelliq-labs/intentdiff-core/blob/main/docs/C_ABI.md)
(`intentdiff_call` / `intentdiff_free`), confined per-call arenas for argument strings, and a
shared arena holding the library lookup.

- `IntentDiff.java` — `load(Path)`, `callRaw(name, argsJsonArray)` returning the raw envelope
  JSON (the scaffold deliberately has no JSON dependency; a typed layer arrives with one), and
  `AutoCloseable` lifecycle.
- `Smoke.java` — the end-to-end proof harness (version, supports_language, and the
  missing-arg `value_error` envelope).

Contract facts the smoke pins: missing positional arg → `value_error`; malformed `args_json`
→ `bad_request`; every returned pointer is `reinterpret`ed, copied, then freed.

FFM is preview in JDK 21 (`--enable-preview`, plus `--enable-native-access=ALL-UNNAMED` to
silence the restricted-method warning); final from JDK 22.
