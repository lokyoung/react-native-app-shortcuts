import React, { Component } from 'react';
import { Text, View } from 'react-native';
import { Actions } from 'react-native-router-flux'
import styles from './styles'

export default class HomePage extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text onPress={Actions.detailPage}>Home</Text>
      </View>
    )
  }
}