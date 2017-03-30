# react-native-app-shortcuts

## Getting started

`$ npm install react-native-app-shortcuts --save`

### Mostly automatic installation

`$ react-native link react-native-app-shortcuts`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNAppShortcutsPackage;` to the imports at the top of the file
  - Add `new RNAppShortcutsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-app-shortcuts'
  	project(':react-native-app-shortcuts').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-app-shortcuts/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-app-shortcuts')
  	```
## Usage
```javascript
import RNAppShortcuts from 'react-native-app-shortcuts';

// TODO: What to do with the module?
RNAppShortcuts;
```
  