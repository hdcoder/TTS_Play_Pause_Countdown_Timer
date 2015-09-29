# TTS_Play_Pause_Countdown_Timer
Implementing TTS with play pause using media player and a countdown timer.

(Screen A) A splash screen with just an app icon, then after 3 seconds land to screen B.
(Screen B) In screen B simply put a button called “GO”, onClick should go to screen C.
(Screen C) Screen C should have all of the following items.
* An arbitrary photo.
* A timer start to countdown immediately from 15 seconds, displaying each counting seconds until 0. Play a simple sound at each of the last 3 seconds, i.e. ding, ding, ding. Go back to screen B once countdown finishes.
* Four ImageButtons:
A Question Mark button, onClick will display few lines of texts. You can make the text display via fragment or dialog. The user should be able to dismiss this fragment/dialog and go back to screen C.
A Play button, onClick should play the TextToSpeech of the same text in the Question Mark button, in the user’s default language. Alert user to download the TTS language if their system doesn’t have it.
A Pause button, this button should appear only upon the Play button is pressed. The Pause button should pauses the TTS, the countdown timer if it’s counting, and display the Play button. After that if we press the Play button, the TTS should resume playing, and the counter should resume counting down.
A Stop button, onClick should stop the TextToSpeech.
