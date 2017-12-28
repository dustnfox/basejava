package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.exception.StorageException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class DataStreamSerializer implements StreamSerializer {

    private static final String NULL_STRING = "";

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            SectionWriter sw = new SectionWriter(dos, NULL_STRING);
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            //Contacts
            Set<Map.Entry<ContactType, String>> contacts = r.getContacts().entrySet();
            sw.writeInt(contacts.size());
            sw.writeContacts(contacts);
            //Sections
            Set<Map.Entry<SectionType, Section>> sections = r.getSections().entrySet();
            sw.writeInt(sections.size());
            sw.writeTextSections(sections);
            sw.writeListSections(sections);
            sw.writeOrganizationSections(sections);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            SectionReader sr = new SectionReader(dis, NULL_STRING);
            Resume r = sr.getResume();
            //Contacts read
            sr.readContacts(r);
            //Sections read
            sr.readSections(r);
            return r;
        }
    }

    private static class SectionWriter {

        @FunctionalInterface
        private interface DSWriteOperation<T> {
            void makeWrite(T t) throws IOException;
        }

        private DataOutputStream DOS = null;

        private String NULL_STRING;

        SectionWriter(DataOutputStream dos, String nullString) {
            DOS = dos;
            NULL_STRING = nullString;
        }

        private <T> void operation(T s, DSWriteOperation<T> func) {
            try {
                func.makeWrite(s);
            } catch (IOException e) {
                throw new StorageException("Data stream write error", e);
            }
        }

        private void writeString(String string) {
            operation(string, s -> DOS.writeUTF(s));
        }

        private void writeStringOrNull(String string) {
            operation(string, s -> {
                if (s != null)
                    DOS.writeUTF(s);
                else
                    DOS.writeUTF(NULL_STRING);
            });
        }

        private void writeInt(int i) {
            operation(i, n -> DOS.writeInt(n));
        }

        private void makeSectionWrite(Map.Entry<SectionType, Section> e
                , Consumer<Map.Entry<SectionType, Section>> func) {
            if (e.getValue() != null) {
                writeString(e.getKey().name());
                if (e.getValue() != null) {
                    writeString(e.getValue().getClass().getName());
                    func.accept(e);
                }
            }
        }

        void writeContacts(Set<Map.Entry<ContactType, String>> contacts) {
            contacts.forEach(c -> {
                writeString(c.getKey().name());
                writeStringOrNull(c.getValue());
            });
        }

        void writeTextSections(Set<Map.Entry<SectionType, Section>> sections) {
            sections.stream()
                    .filter(s -> s.getValue() instanceof TextSection)
                    .forEach(e -> makeSectionWrite(e, t -> {
                        TextSection ts = (TextSection) t.getValue();
                        writeStringOrNull(ts.getContent());
                    }));
        }

        void writeListSections(Set<Map.Entry<SectionType, Section>> sections) {
            sections.stream()
                    .filter(s -> s.getValue() instanceof ListSection)
                    .forEach(e -> makeSectionWrite(e, t -> {
                        ListSection ls = (ListSection) t.getValue();
                        writeInt(ls.getItems().size());
                        ls.getItems().forEach(this::writeString);
                    }));
        }

        void writeOrganizationSections(Set<Map.Entry<SectionType, Section>> sections) {
            sections.stream()
                    .filter(s -> s.getValue() instanceof OrganizationSection)
                    .forEach(e -> makeSectionWrite(e, t -> {
                        OrganizationSection os = (OrganizationSection) t.getValue();
                        writeInt(os.getOrganizations().size());
                        os.getOrganizations().forEach(o -> {
                            writeString(o.getHomePage().getName());
                            writeStringOrNull(o.getHomePage().getUrl());
                            writeInt(o.getPositions().size());
                            o.getPositions().forEach(p -> {
                                writeString(p.getStartDate().toString());
                                writeString(p.getEndDate().toString());
                                writeString(p.getTitle());
                                writeStringOrNull(p.getDescription());

                            });
                        });
                    }));
        }
    }


    private static class SectionReader {
        @FunctionalInterface
        private interface DSReadOperation<T> {
            T makeRead() throws IOException;
        }

        @FunctionalInterface
        private interface ReadElement<T> {
            T readElement();
        }

        private DataInputStream DIS = null;

        private String NULL_STRING;

        SectionReader(DataInputStream dis, String nullString) {
            DIS = dis;
            NULL_STRING = nullString;
        }

        private <T> T operation(DSReadOperation<T> func) {
            try {
                return func.makeRead();
            } catch (IOException e) {
                throw new StorageException("Data stream write error", e);
            }
        }

        private String readString() {
            return operation(() -> DIS.readUTF());
        }

        private String readStringOrNull() {
            return operation(() -> {
                String data = DIS.readUTF();
                if (data.equals(NULL_STRING))
                    return null;
                else
                    return data;
            });
        }

        private int readInt() {
            return operation(() -> DIS.readInt());
        }

        public Resume getResume() {
            String uuid = readString();
            String fullName = readString();
            return new Resume(uuid, fullName);
        }

        void readContacts(Resume r) {
            int n = readInt();
            for (int i = 0; i < n; i++) {
                r.addContact(ContactType.valueOf(readString()), readStringOrNull());
            }
        }

        void readSections(Resume r) {
            int n = readInt();
            for (int i = 0; i < n; i++) {
                SectionType sectionType = SectionType.valueOf(readString());
                Section section;
                String className = readString();
                if (className.equals(TextSection.class.getName())) {
                    section = readTextSection();
                } else if (className.equals(ListSection.class.getName())) {
                    section = readListSection();
                } else {
                    section = readOrganizationSection();
                }
                r.addSection(sectionType, section);
            }
        }

        private TextSection readTextSection() {
            return new TextSection(readString());
        }

        private ListSection readListSection() {
            return new ListSection(readListOfElements(this::readString));
        }

        private OrganizationSection readOrganizationSection() {
            return new OrganizationSection(readListOfElements(() -> {
                String orgName = readString();
                String orgURL = readStringOrNull();
                return new Organization(new Link(orgName, orgURL), readListOfElements(() -> {
                    LocalDate startDate = LocalDate.parse(readString());
                    LocalDate endDate = LocalDate.parse(readString());
                    String title = readString();
                    String description = readStringOrNull();
                    return new Organization.Position(startDate, endDate, title, description);
                }));
            }));
        }


        private <T> List<T> readListOfElements(ReadElement<T> func) {
            int n = readInt();
            List<T> list = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                list.add(func.readElement());
            }
            return list;
        }
    }
}
