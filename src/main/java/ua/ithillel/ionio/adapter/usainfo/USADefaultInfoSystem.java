package ua.ithillel.ionio.adapter.usainfo;

import ua.ithillel.ionio.adapter.info.InfoSystem;
import ua.ithillel.ionio.adapter.info.Information;

public class USADefaultInfoSystem implements USAInfoSystem {
    @Override
    public Information getInfo(String firstName, String lastName) {
        return new Information("Info for " + firstName + " " + lastName);
    }
}
