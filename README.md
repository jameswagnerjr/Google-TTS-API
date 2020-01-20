This is a HTTP RESTful API that serves as a wrapper around google's tts service. The intent was to create something capable of being used for home assistant, while still allowing me to set the voice to match google assistant's voice.

It's pretty simple & lightweight, meant to run on the same server as [Home Assistant](https://www.home-assistant.io/) (Even though that's non-optimal). It passes text to Google's TTS engine, which returns some audio. The audio is saved, and because home assistant isn't the smartest, it stores the audio & exposes another endpoint containing the audio as an mp3. This wasn't meant to scale, so it can only handle one piece of text at a time.

## Potential Future Improvements
* Remove past mp3s on obtaining a new mp3.
* Support for multiple users/key:value pairings. Basically, a way for HomeAssistant to handle multiple speakers at the same time