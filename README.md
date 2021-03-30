# ApplicationRecord_Android_Java
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/login.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/main.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/audiorecord.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/audioList.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/audioPlayer.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/videoList.jpg) 
![alt tag](https://github.com/MinhThien99/ApplicationRecord_Android_Java/blob/ThienMaster/Image/videoPlayer.jpg) 

# Android Recording Library
Android Recording library offers convenient tools for audio/video recording and playback.

+ For audio it uses:
 - AudioRecord to capture and save audio signal from microphone
 - MediaPlayer with MediaController to play recorded audio
   custom Visualizer (like bar chart) to represent audio signal on screen while recording and during playback
+ For video it uses:
  - Camera and MediaRecorder to record a video of specified resolution
  - MediaPlayer with MediaController to play recorded video
    custom SurfaceView with adjustable size to properly display Camera preview and recorded video (in portrait and landscape modes)

# Features:
+ Recording audio/video
+ Playing back recordings
+ Make recordings in Mp3/Mp4(It can support WAV format at 48Khz)
+ Recording and playing back in background
+ Rename/Delete recordings
+ Share recordings
+ List recordings
+ Color themes

# Techs
+ Java
+ Android Jetpack(Navigation, ViewModel,...)
+ SQLite, External Storage
