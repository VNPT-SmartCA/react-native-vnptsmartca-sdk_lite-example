/**
 * @format
 */

import 'react-native';
import React from 'react';
import App from '../App';

// Note: import explicitly to use the types shipped with jest.
import {it, jest} from '@jest/globals';

// Note: test renderer must be required after react-native.
import renderer from 'react-test-renderer';

jest.mock('react-native', () => {
  return {
    Alert: {
      alert: () => {},
    },
    Button: 'Button',
    NativeModules: {
      SmartCAModule: {
        getAuth: () => {},
        getWaitingTransaction: () => {},
      },
    },
    NativeEventEmitter: class {
      addListener() {
        return {
          remove() {},
        };
      }
    },
    SafeAreaView: 'SafeAreaView',
    ScrollView: 'ScrollView',
    StyleSheet: {
      create: (styles: unknown) => styles,
    },
    Text: 'Text',
    TextInput: 'TextInput',
    View: 'View',
  };
});

it('renders correctly', () => {
  renderer.create(<App />);
});
