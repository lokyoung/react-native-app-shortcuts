import React, { Component } from 'react';
import { Text, View } from 'react-native';
import { Actions } from 'react-native-router-flux'
import styles from './styles'
import RNAppShortcuts from 'react-native-app-shortcuts';

export default class DetailPage extends Component {
  render() {
    RNAppShortcuts.addShortcut({
      id: '1',
      shortLabel: 'detail',
      longLabel: 'Open detail',
      iconFolderName: 'drawable',
      iconName: 'icon'
    })

    return (
      <View style={styles.container}>
        <Text>Detail</Text>
      </View>
    )
  }
}