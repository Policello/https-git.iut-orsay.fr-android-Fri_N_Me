package fr.iut_orsay.frinme.model;

import java.util.Comparator;

import fr.iut_orsay.frinme.view.Contact;

/**
 * Created by yyang5 on 16/03/2018.
 */

public class ContactComparator {

    public static Comparator<ContactModel> getContactNomComparator() {
        return new ContactComparator.ContactNomComparator();
    }

    public static Comparator<ContactModel> getContactPrenomComparator() {
        return new ContactComparator.ContactPrenomComparator();
    }





    private static class ContactNomComparator implements Comparator<ContactModel> {
        @Override
        public int compare(ContactModel contactModel, ContactModel t1) {
            return contactModel.getNom().compareTo(t1.getNom());
        }
    }

    private static class ContactPrenomComparator implements Comparator<ContactModel> {


        @Override
        public int compare(ContactModel contactModel, ContactModel t1) {
            return contactModel.getPrenom().compareTo(t1.getPrenom());
        }
    }


}
