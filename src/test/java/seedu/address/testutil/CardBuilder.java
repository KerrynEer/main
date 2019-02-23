package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.card.Address;
import seedu.address.model.card.Card;
import seedu.address.model.card.Email;
import seedu.address.model.card.Name;
import seedu.address.model.card.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Card objects.
 */
public class CardBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    public CardBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the CardBuilder with the data of {@code cardToCopy}.
     */
    public CardBuilder(Card cardToCopy) {
        name = cardToCopy.getName();
        phone = cardToCopy.getPhone();
        email = cardToCopy.getEmail();
        address = cardToCopy.getAddress();
        tags = new HashSet<>(cardToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Card} that we are building.
     */
    public CardBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Card} that we are building.
     */
    public CardBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Card} that we are building.
     */
    public CardBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Card} that we are building.
     */
    public CardBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Card} that we are building.
     */
    public CardBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Card build() {
        return new Card(name, phone, email, address, tags);
    }

}
