# intentdiff-java

The **thin Java binding** for the IntentDiff engine
([intentdiff-core](https://github.com/buchochelliq-labs/intentdiff-core)), built on
java.lang.foreign (FFM): downcalls to the engine's stable C ABI (`intentdiff_call` /
`intentdiff_free`), zero functional work in the binding.

```java
try (IntentDiff engine = IntentDiff.load(Path.of(lib))) {
    String envelope = engine.callRaw("version", "[]");
}
```

Build + smoke (JDK 21 uses FFM as preview; final from JDK 22):

```bash
javac --release 21 --enable-preview -d out src/dev/intentdiff/*.java
java --enable-preview -cp out dev.intentdiff.Smoke $INTENTDIFF_CORE_LIB
```

Scaffold status: raw-envelope API (no JSON dependency yet); a typed layer lands as the
binding grows. Authored fresh for the #82 split; the monorepo
(`buchochelliq-labs/intentdiff`) remains the archive of record. License: MIT.
