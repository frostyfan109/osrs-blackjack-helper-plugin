# Blackjack Helper Plugin
OpenOSRS plugin that consecutively highlights knock-out option upon failure.

## Example
<img src="/example.gif?raw=true" width="400px">

## Installation
This plugin is old and has not been ported to the newer design style introduced to OpenOSRS during the external plugins rework. It can be installed by copying the compiled jar found in `build/libs` to the `plugins` folder in the `.openosrs` directory.
### Windows
Assuming `.openosrs` is located in the user profile:
```
mkdir %UserProfiles%/.openosrs/plugins
copy build/libs/{JAR_NAME} %UserProfile%/.openosrs/plugins
```

### Linux
Assuming `.openosrs` is located in the home directory:
```
make install
```

Of course, `{JAR_NAME}` should be replaced with the compiled jar's file name found under `build/libs`.