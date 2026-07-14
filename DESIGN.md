# Text Echo App — Design Notes

## Architecture

MVVM with a light clean-architecture split into three layers, all inside one
Gradle module (a second module would be over-engineering):

```
echo/
  domain/         NameValidator, NameValidationResult, NameRepository (interface)
  data/           FakeRemoteNameRepository (implements NameRepository)
  presentation/   EchoUiState, EchoViewModel, EchoScreen (Compose)
```

## Layers deliberately left out

Classic clean architecture usually adds more seams than this app needs.
Left out on purpose:

- **Use-case / interactor classes**
- **Multi-module split**
- **DI framework (Hilt/Koin)** — The trade-off: `EchoViewModel(repository: NameRepository = FakeRemoteNameRepository())` this is the one spot where presentation (the ViewModel)
  directly imports a concrete data-layer class instead of only the domain
  interface, so the dependency direction isn't perfectly one-way. If a real DI framework were introduced later, this import would disappear.

The one seam kept is the `NameRepository` interface between `domain` and
`data`, because that's the boundary the task explicitly calls for ("pretend
to validate... with an external server"): it lets `FakeRemoteNameRepository`
be swapped for a real HTTP-backed implementation without touching the
ViewModel or UI. Every other layer was skipped because it would be over-engineering.

## Validation rules

Basic human-name rules, applied after trimming:
- non-empty
- 2–50 characters
- only Unicode letters, spaces, hyphens and apostrophes, and must start/end
  with a letter (`^\p{L}[\p{L}' -]*\p{L}$`)

`\p{L}` (rather than `[A-Za-z]`) so names like "José" or "Renée" are accepted;