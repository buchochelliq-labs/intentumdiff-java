# Contributing to intentumdiff-java

- The binding stays thin: no semantics — new engine capability lands in intentumdiff-core and
  surfaces here as typed methods over the raw envelope call.
- Natural next steps: a typed envelope layer (bring a JSON library), a Maven/Gradle build, and
  JDK 22+ non-preview support.
- Compile + smoke per docs/BUILDING.md before proposing changes.
