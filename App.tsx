import React, {useEffect, useState} from 'react';
import {
  Alert,
  Button,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
  NativeEventEmitter,
  NativeModules,
} from 'react-native';

const {SmartCAModule} = NativeModules;

function App() {
  const [customerId, setCustomerId] = useState('');
  const [transactionId, setTransactionId] = useState('');

  const handleGetAuth = () => {
    if (!customerId.trim()) {
      Alert.alert('Loi', 'Vui long nhap so CCCD');
      return;
    }

    SmartCAModule.getAuth(customerId.trim());
  };

  const handleGetWaitingTransaction = () => {
    if (!transactionId.trim()) {
      Alert.alert('Loi', 'Vui long nhap transaction id');
      return;
    }

    SmartCAModule.getWaitingTransaction(transactionId.trim());
  };

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(SmartCAModule);
    const eventListener = eventEmitter.addListener('EventReminder', event => {
      const code = event.code;
      const title = code === 0 ? 'Success' : 'Error';
      const token = event.token ?? '';
      const credentialId = event.credentialId ?? '';
      const serial = event.serial ? `\nserial: ${event.serial}` : '';
      const statusCode = event.statusCode ?? token;
      const statusDesc = event.statusDesc ?? credentialId;
      const data = event.data ? `\ndata: ${event.data}` : '';
      const message = event.statusCode
        ? `statusCode: ${statusCode}\nstatusDesc: ${statusDesc}${data}`
        : code === 0
          ? `token: ${token}\ncredentialId: ${credentialId}${serial}`
          : `statusCode: ${statusCode}\nstatusDesc: ${statusDesc}`;

      Alert.alert(title, message);
    });

    return () => {
      eventListener.remove();
    };
  }, []);

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>VNPT SmartCA SDK Lite Example</Text>
        <TextInput
          style={styles.input}
          onChangeText={setCustomerId}
          value={customerId}
          placeholder="Số CCCD (uid)"
        />
        <Button onPress={handleGetAuth} title="Get Auth" color="#0f62fe" />
        <TextInput
          style={styles.input}
          onChangeText={setTransactionId}
          value={transactionId}
          placeholder="Transaction ID"
        />
        <View style={styles.buttonSpacing}>
          <Button
            onPress={handleGetWaitingTransaction}
            title="Get Waiting Transaction"
            color="#24a148"
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#f4f7fb',
  },
  container: {
    padding: 20,
    gap: 12,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    color: '#111827',
  },
  description: {
    fontSize: 14,
    lineHeight: 20,
    color: '#4b5563',
  },
  input: {
    height: 48,
    borderWidth: 1,
    borderColor: '#d1d5db',
    borderRadius: 10,
    paddingHorizontal: 12,
    backgroundColor: '#ffffff',
  },
  buttonSpacing: {
    marginBottom: 12,
  },
});

export default App;
