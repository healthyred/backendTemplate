package backendTemplate.dagger;

import backendTemplate.handler.GetHealthHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {WebModule.class})
public interface ApplicationComponent {

    GetHealthHandler getHealthHandler();

}
