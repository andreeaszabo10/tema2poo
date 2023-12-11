package app.player;

import app.utils.Enums;
import lombok.Getter;

@Getter
public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private String repeat;
    private final boolean shuffle;
    @Getter
    private boolean paused;

    public PlayerStats(final String name,
                       final int remainedTime,
                       final Enums.RepeatMode repeatMode,
                       final boolean shuffle,
                       final boolean paused) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.paused = paused;
        switch (repeatMode) {
            case REPEAT_ALL -> {
                this.repeat = "Repeat All";
            }
            case REPEAT_ONCE -> {
                this.repeat = "Repeat Once";
            }
            case REPEAT_INFINITE -> {
                this.repeat = "Repeat Infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                this.repeat = "Repeat Current Song";
            }
            case NO_REPEAT -> {
                this.repeat = "No Repeat";
            }
            default -> {
                this.repeat = "";
            }
        }
        this.shuffle = shuffle;
    }


    /**
     * sets paused
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
}
