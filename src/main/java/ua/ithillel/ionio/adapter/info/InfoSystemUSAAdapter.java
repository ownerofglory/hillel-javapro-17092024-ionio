package ua.ithillel.ionio.adapter.info;

import ua.ithillel.ionio.adapter.usainfo.USAInfoSystem;

public class InfoSystemUSAAdapter implements InfoSystem{
    private USAInfoSystem usaInfoSystem;

    public InfoSystemUSAAdapter(USAInfoSystem usaInfoSystem) {
        this.usaInfoSystem = usaInfoSystem;
    }

    @Override
    public Information getInformation(String fullName) {
        String[] s = fullName.split(" ");
        String fistName = s[0];
        String lastName = s[1];
        return usaInfoSystem.getInfo(fistName, lastName);
    }
}
