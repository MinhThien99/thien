# ApplicationRecord_Android_Java
Android Recording library offers convenient tools for audio/video recording and playback.

For audio it uses:

AudioRecord to capture and save audio signal from microphone
MediaPlayer with MediaController to play recorded audio
custom Visualizer (like bar chart) to represent audio signal on screen while recording and during playback
For video it uses:

Camera and MediaRecorder to record a video of specified resolution
MediaPlayer with MediaController to play recorded video
custom SurfaceView with adjustable size to properly display Camera preview and recorded video (in portrait and landscape modes)
