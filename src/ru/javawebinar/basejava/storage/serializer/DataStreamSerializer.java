package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    private static final String NULL_STRING = "";

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> contact : contacts.entrySet()) {
                if (contact.getValue() != null) {
                    dos.writeUTF(contact.getKey().name());
                    dos.writeUTF(contact.getValue());
                }
            }

            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> section : sections.entrySet()) {
                SectionType sectionType = section.getKey();
                dos.writeUTF(sectionType.name());

                if (section.getValue() == null) { // has Section?
                    dos.writeBoolean(false);
                } else {
                    dos.writeBoolean(true);

                    switch (sectionType) {
                        case PERSONAL:
                        case OBJECTIVE:
                            dos.writeUTF(((TextSection) section.getValue()).getContent());
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            ListSection listSection = (ListSection) section.getValue();
                            dos.writeInt(listSection.getItems().size());
                            for (String s : listSection.getItems()) {
                                dos.writeUTF(s);
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section.getValue();
                            dos.writeInt(orgSection.getOrganizations().size());

                            for (Organization org : orgSection.getOrganizations()) {
                                dos.writeUTF(org.getHomePage().getName());
                                String url = org.getHomePage().getUrl();
                                dos.writeUTF(url != null ? url : NULL_STRING);
                                dos.writeInt(org.getPositions().size());

                                for (Organization.Position pos : org.getPositions()) {
                                    dos.writeUTF(pos.getStartDate().toString());
                                    dos.writeUTF(pos.getEndDate().toString());
                                    dos.writeUTF(pos.getTitle());
                                    String posDescr = pos.getDescription();
                                    dos.writeUTF(posDescr != null ? posDescr : NULL_STRING);
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume r = new Resume(uuid, fullName);

            int contactsCount = dis.readInt();
            while (contactsCount-- > 0) {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String contactInfo = dis.readUTF();
                r.addContact(contactType, contactInfo);
            }

            int sectionsCount = dis.readInt();
            for (int i = 0; i < sectionsCount; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                boolean hasSection = dis.readBoolean();
                Section section = null;
                if (hasSection) {
                    switch (sectionType) {
                        case PERSONAL:
                        case OBJECTIVE:
                            String content = dis.readUTF();
                            section = new TextSection(content);
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            int itemsCount = dis.readInt();
                            List<String> items = new ArrayList<>(itemsCount);
                            while (itemsCount-- > 0) {
                                items.add(dis.readUTF());
                            }
                            section = new ListSection(items);
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            int organizationsCount = dis.readInt();
                            List<Organization> organizations = new ArrayList<>(organizationsCount);

                            while (organizationsCount-- > 0) {
                                String name = dis.readUTF();
                                String url = dis.readUTF();
                                Link link = new Link(name, url.equals(NULL_STRING) ? null : url);

                                int positionsCount = dis.readInt();
                                List<Organization.Position> positions = new ArrayList<>(positionsCount);

                                while (positionsCount-- > 0) {
                                    LocalDate startDate = LocalDate.parse(dis.readUTF());
                                    LocalDate endDate = LocalDate.parse(dis.readUTF());
                                    String title = dis.readUTF();
                                    String description = dis.readUTF();
                                    Organization.Position position = new Organization.Position(
                                            startDate,
                                            endDate,
                                            title,
                                            description.equals(NULL_STRING) ? null : description);
                                    positions.add(position);
                                }

                                organizations.add(new Organization(link, positions));

                            }

                            section = new OrganizationSection(organizations);

                            break;
                    }
                }
                r.addSection(sectionType, section);
            }

            return r;
        }
    }
}
