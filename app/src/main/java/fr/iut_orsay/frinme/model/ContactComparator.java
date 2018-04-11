package fr.iut_orsay.frinme.model;

import java.util.Comparator;

/**
 * Classe permettant de comparer deux contacts
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
