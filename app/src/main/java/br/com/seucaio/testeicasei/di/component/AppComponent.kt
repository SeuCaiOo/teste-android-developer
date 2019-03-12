package br.com.seucaio.testeicasei.di.component

import android.app.Application
import br.com.seucaio.testeicasei.di.module.NetworkModule
import br.com.seucaio.testeicasei.iCaseiApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: iCaseiApp): iCaseiApp
}