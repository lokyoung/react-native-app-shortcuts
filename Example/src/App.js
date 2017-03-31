import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { Router, Scene, Actions } from 'react-native-router-flux'
import RNAppShortcuts from 'react-native-app-shortcuts';

import HomePage from './HomePage';
import DetailPage from './DetailPage';

export default class Example extends Component {
  componentWillMount() {
    RNAppShortcuts.handleShortcut((id) => {
      if (id === '1') {
        // go to detail page
        Actions.detailPage()
      }
    })
  }

  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene key="homePage" component={HomePage} title="Home" initial={true} />
          <Scene key="detailPage" component={DetailPage} title="Detail" />
        </Scene>
      </Router>
    )
  }
}
