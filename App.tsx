/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  useColorScheme,
  View,
  Button,
  Alert,
  NativeEventEmitter,
  NativeModules,
  TextInput,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

const {SmartCAModule} = NativeModules;

// const onPress = () => {
//   SmartCAModule.createCalendarEvent('testName', 'testLocation');
// };

function App() {
  const [uid, setUid] = React.useState('');
  const [transactionId, setTransactionId] = React.useState('');

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const onPress2 = function () {
    // CalendarModule.createCalendarEvent('testName', 'testLocation');
    // CalendarModule.increment();
    // console.log(CalendarModule);

    if (!uid.trim()) {
      Alert.alert('Error', 'Vui long nhap So CCCD (uid)');
      return;
    }

    SmartCAModule.getAuth(uid);

    // Alert.alert('test', 'message');
  };

  const onPress4 = function () {
    console.log(transactionId);
    SmartCAModule.getWaitingTransaction(transactionId);

    // Alert.alert('test', 'message');
  };

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(SmartCAModule);
    let eventListener = eventEmitter.addListener('EventReminder', event => {
      // console.log(event.eventProperty);
      let code = event.code;
      let tokenOrStatusCode = event.token;
      let certIdOrStatusDesc = event.credentialId;
      // console.log(code);
      // console.log(tokenOrStatusCode);
      // console.log(certIdOrStatusDesc);

      let message = `
    ${code === 0 ? 'token' : 'status code'}: ${tokenOrStatusCode}
    ${code === 0 ? 'certId' : 'status desc'}: ${certIdOrStatusDesc}
  `;

      console.log(message);

      Alert.alert(code == 1 ? 'Error' : 'Success', message, [
        {text: 'OK', onPress: () => console.log('OK Pressed')},
      ]);

      // Alert.alert('test', tokenOrStatusCode);
    });

    let testIOSEvent = eventEmitter.addListener('onIncrement', event => {
      console.log('onIncrement event', event);
    });

    // Removes the listener once unmounted
    return () => {
      eventListener.remove();
      testIOSEvent.remove();
    };
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        {/* <AppBar></AppBar> */}
        {/* <Header /> */}
        {/* <Button
          onPress={onPress}
          title="Learn More"
          color="#841584"
          accessibilityLabel="Learn more about this purple button"
        /> */}

        <TextInput
          style={styles.input}
          onChangeText={setUid}
          value={uid}
          placeholder="Số CCCD (uid)"
        />
        <View style={styles.button}>
          <Button
            onPress={onPress2}
            title="Get Auth"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        <TextInput
          style={styles.input}
          onChangeText={setTransactionId}
          value={transactionId}
          placeholder="Transaction ID"
        />
        <View style={styles.button}>
          <Button
            onPress={onPress4}
            title="Get Waiting Transaction"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        {/* <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Section title="Step 1">
            Edit <Text style={styles.highlight}>""</Text> to change this screen
            and then come back to see your edits.
          </Section>
          <Section title="See Your Changes">
            <ReloadInstructions />
          </Section>
          <Section title="Debug">
            <DebugInstructions />
          </Section>
          <Section title="Learn More">
            Read the docs to discover what to do next:
          </Section>
          <LearnMoreLinks />
        </View> */}
      </ScrollView>
      {/* <View>
        <Dialog.Container
          visible={visible}
          verticalButtons={true}
          onRequestClose={handleCancel}>
          <Dialog.Title>{code}</Dialog.Title>
          <ScrollView>
            <Dialog.Description>{text}</Dialog.Description>
          </ScrollView>
          <Dialog.Button label="OK" onPress={handleCancel} />
        </Dialog.Container>
      </View> */}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  input: {
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
  },
  button: {
    marginBottom: 12,
  },
});

export default App;
