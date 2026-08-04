# intentumdiff-java

[![CI](https://github.com/buchochelliq-labs/intentumdiff-java/actions/workflows/ci.yml/badge.svg)](https://github.com/buchochelliq-labs/intentumdiff-java/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![JDK 21](https://img.shields.io/badge/JDK-21-red.svg)](https://openjdk.org/)

The **thin Java binding** for the IntentumDiff engine
([intentumdiff-core](https://github.com/buchochelliq-labs/intentumdiff-core)), built on
java.lang.foreign (FFM): downcalls to the engine's stable C ABI (`intentumdiff_call` /
`intentumdiff_free`), zero functional work in the binding.

```java
try (IntentumDiff engine = IntentumDiff.load(Path.of(lib))) {
    String envelope = engine.callRaw("version", "[]");
}
```

Build + smoke (JDK 21 uses FFM as preview; final from JDK 22):

```bash
javac --release 21 --enable-preview -d out src/dev/intentumdiff/*.java
java --enable-preview -cp out dev.intentumdiff.Smoke $INTENTUMDIFF_CORE_LIB
```

Scaffold status: raw-envelope API (no JSON dependency yet); a typed layer lands as the
binding grows. Authored fresh for the #82 split; the monorepo
(`buchochelliq-labs/intentumdiff`) remains the archive of record. License: MIT.
