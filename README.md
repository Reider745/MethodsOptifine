# Methods Optifine

Данный мод предназначен для оптимизации и повышения tps.
С поддержкой локальной, многопользовательской игры, а так-же с поддержкой серверного ядра
[ZoteCoreLoader](https://github.com/Reider745/ZoteCoreLoader/blob/NukkitMot/README-RU.md).

![MethodsOptifine](/src/mod_icon.png)

## Как работает мод?

[Рефлексия](https://javarush.com/groups/posts/513-reflection-api-refleksija-temnaja-storona-java) — это механизм исследования данных о программе во время её выполнения. 
Рефлексия позволяет исследовать информацию о полях, 
методах и конструкторах классов. Это основа работы java классов в [rhino](https://github.com/mozilla/rhino), 
js движок который использует inner core.
Это хороший и универсальный инструмент, но достаточно медленная

Данный мод переписавает часть api inner core, которое используется везде, 
убирая рефлексию, засчет чего и происходит ускорение работы, 
практически с идеальной обратной совместимостью.