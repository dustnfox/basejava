package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    private static final String NULL_STRING = "";

    @FunctionalInterface
    private interface DOSWriter<E> {
        void doElementWrite(DataOutputStream dos, E element) throws IOException;
    }


    private <E> void doCollectionWrite(Collection<E> list, DataOutputStream dos, DOSWriter<E> elementWriter)
            throws IOException {

        dos.writeInt(list.size());
        for (E element : list) {
            elementWriter.doElementWrite(dos, element);
        }
    }

    @FunctionalInterface
    private interface DISReader<E> {
        E doElementRead(DataInputStream dis) throws IOException;
    }


    private <E> List<E> doCollectionRead(DataInputStream dis, DISReader<E> elementReader)
            throws IOException {

        int size = dis.readInt();
        List<E> list = new ArrayList<>(size);
        while (size-- > 0) {
            list.add(elementReader.doElementRead(dis));
        }

        return list;
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Set<Map.Entry<ContactType, String>> contacts = r.getContacts().entrySet();
            doCollectionWrite(contacts, dos, (outStream, element) -> {
                outStream.writeUTF(element.getKey().name());
                if (element.getValue() == null) {
                    outStream.writeBoolean(false);
                } else {
                    outStream.writeBoolean(true);
                    outStream.writeUTF(element.getValue());
                }
            });

            Set<Map.Entry<SectionType, Section>> sections = r.getSections().entrySet();
            doCollectionWrite(sections, dos, (outStream, section) -> {
                SectionType sectionType = section.getKey();
                outStream.writeUTF(sectionType.name());

                if (section.getValue() == null) {
                    outStream.writeBoolean(false);
                } else {
                    outStream.writeBoolean(true);

                    switch (sectionType) {
                        case PERSONAL:
                        case OBJECTIVE:
                            TextSection textSection = (TextSection) section.getValue();
                            outStream.writeUTF(textSection.getContent());
                            break;

                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            ListSection listSection = (ListSection) section.getValue();
                            doCollectionWrite(listSection.getItems(), outStream, DataOutputStream::writeUTF);
                            break;

                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section.getValue();
                            doCollectionWrite(orgSection.getOrganizations(), outStream, (out, org) -> {
                                out.writeUTF(org.getHomePage().getName());
                                String url = org.getHomePage().getUrl();
                                out.writeUTF(url != null ? url : NULL_STRING);
                                doCollectionWrite(org.getPositions(), out, (o, pos) -> {
                                    o.writeUTF(pos.getStartDate().toString());
                                    o.writeUTF(pos.getEndDate().toString());
                                    o.writeUTF(pos.getTitle());
                                    String descr = pos.getDescription();
                                    o.writeUTF(descr != null ? descr : NULL_STRING);
                                });
                            });
                            break;
                    }
                }
            });
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
                boolean hasContact = dis.readBoolean();
                if (hasContact) {
                    String contactInfo = dis.readUTF();
                    r.addContact(contactType, contactInfo);
                }
            }

            int sectionsCount = dis.readInt();
            while (sectionsCount-- > 0) {
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
                            List<String> items = doCollectionRead(dis, DataInput::readUTF);
                            section = new ListSection(items);
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            List<Organization> organizations;
                            organizations = doCollectionRead(dis, (inputStream) -> {
                                String name = inputStream.readUTF();
                                String url = inputStream.readUTF();
                                Link link = new Link(name, url.equals(NULL_STRING) ? null : url);

                                return new Organization(link, doCollectionRead(inputStream, (in) -> {
                                    LocalDate startDate = LocalDate.parse(in.readUTF());
                                    LocalDate endDate = LocalDate.parse(in.readUTF());
                                    String title = in.readUTF();
                                    String descr = in.readUTF();
                                    return new Organization.Position(startDate
                                            , endDate
                                            , title
                                            , descr.equals(NULL_STRING) ? null : descr);
                                }));
                            });
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
