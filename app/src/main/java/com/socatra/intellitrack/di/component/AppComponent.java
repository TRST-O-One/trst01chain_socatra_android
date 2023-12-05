package com.socatra.intellitrack.di.component;

import android.app.Application;

import com.socatra.intellitrack.dagger.App;
import com.socatra.intellitrack.di.module.ActivityModule;
import com.socatra.intellitrack.di.module.AppModule;
import com.socatra.intellitrack.di.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton


@Component(modules={AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    void inject(App app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }


}
