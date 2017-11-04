package rx.plugins;

/**
 * Created by Pawel_Piedel on 04.11.2017.
 */

public class RxJavaTestPlugins extends RxJavaPlugins {
    RxJavaTestPlugins() {
        super();
    }

    public static void resetPlugins() {
        getInstance().reset();
    }
}
