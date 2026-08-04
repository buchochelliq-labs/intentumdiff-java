# Building intentumdiff-java

Toolchain: **JDK 21** (FFM as preview; final from JDK 22).

```bash
javac --release 21 --enable-preview -d out src/dev/intentumdiff/*.java
```

Smoke against a real engine library from
[intentumdiff-core](https://github.com/buchochelliq-labs/intentumdiff-core):

```bash
java --enable-preview --enable-native-access=ALL-UNNAMED -cp out \
     dev.intentumdiff.Smoke /path/to/intentumdiff_rust_core.dll
# prints the version/supports_language envelopes and SMOKE OK
```

(`INTENTUMDIFF_CORE_LIB` works in place of the argument.)
