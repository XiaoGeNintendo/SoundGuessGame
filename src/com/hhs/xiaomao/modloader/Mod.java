package com.hhs.xiaomao.modloader;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	String modid();
	String name();
	String version();
}