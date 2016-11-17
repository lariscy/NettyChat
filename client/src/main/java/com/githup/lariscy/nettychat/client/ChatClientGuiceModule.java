package com.githup.lariscy.nettychat.client;

import com.google.inject.AbstractModule;
import net.engio.mbassy.bus.MBassador;

/**
 * @author Steven
 */
public class ChatClientGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MBassador.class).toInstance(new MBassador());
    }

}
