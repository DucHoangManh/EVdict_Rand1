package sample;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.Voice;

public class Speaker {
    private VoiceManager voiceManager;
    private Voice voice;
    public Speaker(){
        System.setProperty("mbrola.base", "mbrola/");
        voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("mbrola_us1");
    }
    public void speak(String word){
        if (word.equals("")){
                return;}
        try {
            voice.allocate();
            voice.speak(word);
            voice.deallocate();
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }

    }

}
