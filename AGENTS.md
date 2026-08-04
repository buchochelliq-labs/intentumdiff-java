# Agent instructions — intentumdiff-java

The thin Java binding (java.lang.foreign). Zero functional work.

## Hard invariants
- No semantics here; the raw-envelope API grows typed layers, engine work goes upstream.
- Returned pointers: reinterpret -> copy -> intentumdiff_free, always.
- JDK 21 preview flags (--enable-preview, --enable-native-access=ALL-UNNAMED); final in 22+.

Smoke: java ... dev.intentumdiff.Smoke <engine lib> (prints SMOKE OK).
Contract: https://github.com/buchochelliq-labs/intentumdiff-core/blob/main/docs/C_ABI.md
