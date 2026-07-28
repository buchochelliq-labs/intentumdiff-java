# Building intentdiff-java

Toolchain: **JDK 21** (FFM as preview; final from JDK 22).

```bash
javac --release 21 --enable-preview -d out src/dev/intentdiff/*.java
```

Smoke against a real engine library from
[intentdiff-core](https://github.com/buchochelliq-labs/intentdiff-core):

```bash
java --enable-preview --enable-native-access=ALL-UNNAMED -cp out \
     dev.intentdiff.Smoke /path/to/intentdiff_rust_core.dll
# prints the version/supports_language envelopes and SMOKE OK
```

(`INTENTDIFF_CORE_LIB` works in place of the argument.)
