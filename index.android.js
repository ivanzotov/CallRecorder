import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Button,
  View,
  Text,
  Alert,
} from 'react-native'

import { AudioRecorder, AudioUtils } from 'react-native-audio'
import Sound from 'react-native-sound'

const audioPath = AudioUtils.DocumentDirectoryPath + '/record.aac'

export default class CallRecorder extends Component {
  onPress = () => {
    // These timeouts are a hacky workaround for some issues with react-native-sound.
    // See https://github.com/zmxv/react-native-sound/issues/89.
    setTimeout(() => {
      const sound = new Sound(audioPath, '')

      setTimeout(() => {
        sound.play((success) => {
          if (!success) Alert.alert('Error', 'no records found')
        })
      }, 100)
    }, 100)
  }

  render() {
    return (
      <View style={styles.container}>
        <Button title='Play the last record' onPress={this.onPress} />
      </View>
    );
  }
}

const Rec = async (data) => {
  if (data.state === 'extra_state_offhook') {
    AudioRecorder.prepareRecordingAtPath(audioPath, {
      SampleRate: 22050,
      Channels: 1,
      AudioQuality: "Low",
      AudioEncoding: "aac"
    })

    await AudioRecorder.startRecording()
  } else if (data.state === 'extra_state_idle') {
    await AudioRecorder.stopRecording()
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});

AppRegistry.registerHeadlessTask('Rec', () => Rec);
AppRegistry.registerComponent('CallRecorder', () => CallRecorder);
