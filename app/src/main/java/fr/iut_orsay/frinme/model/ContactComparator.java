package fr.iut_orsay.frinme.model;

import java.util.Comparator;

/**
 * Created by yyang5 on 16/03/2018.
 */

public class ContactComparator {

    public static Comparator<ContactModel> getContactPseudoComparator() {
        return new ContactComparator.ContactPseudoComparator();
    }

    private static class ContactPseudoComparator implements Comparator<ContactModel> {
        @Override
        public int compare(ContactModel contactModel, ContactModel t1) {
            return contactModel.getPseudo().compareTo(t1.getPseudo());
        }
    }


}
