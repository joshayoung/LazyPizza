# LazyPizza
Android Application for Pizza Orders

### Notes
* `io.coil-kt.coil3:coil-network-okhttp` is needed to get `AsyncImage` working with `coil`.

### Ktlint
* `./gradlew ktlintCheck` - generate reports
* `./gradlew ktlintFormat` - format code

### Add Firebase Storage
* Run through the setup: https://firebase.google.com/docs/storage/android/start
* Add your config file here: `app/google-services.json`
  * Add this to .gitignore (at least for now).
* Console: https://console.firebase.google.com
* Storage: https://console.firebase.google.com/project/lazypizza-22122/storage/lazypizza-22122.firebasestorage.app/files

### Generate SHA-256 Key
* `./gradlew signingReport`

### Resources
* [Font](https://fonts.google.com/specimen/Instrument+Sans)
* https://firebase.google.com/docs/storage/android/start
* [Image Caching with Coil Compose - Everything You Need to Know](https://www.youtube.com/watch?v=qQVCtkg-O7w)
* [Async with Token](https://github.com/coil-kt/coil/discussions/2558#discussioncomment-10963528)

### Provided Resources
* [Adding Shadows in Compose](https://developer.android.com/develop/ui/compose/graphics/draw/shadows)
* [State in Jetpack Compose](https://developer.android.com/codelabs/jetpack-compose-state#0)
* [Where to hoist state](https://developer.android.com/develop/ui/compose/state-hoisting)
* [Stateful vs Stateless Composables](https://www.youtube.com/watch?v=C8IfGDrmwiE)