package backendTemplate.dagger;

import backendTemplate.handler.GetHealthHandler;
import dagger.Module;
import dagger.Provides;

@Module
public class WebModule {

    @Provides
    public GetHealthHandler healthHandler() {
        return new GetHealthHandler();
    }
}
