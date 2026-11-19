package ChocAn.src.chocan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores every member and saves them to data/members.txt.
 * Assigns new 9-digit member numbers. On the first run, when the file 
 * does not exist yet, it starts with sample members.
 * 
 * @author Aaron Suarez
 */
public class MemberRegistry {
    private static final int FIRST_NUMBER = 100000001;

    private final Path fileName;
    private int lastNumber = FIRST_NUMBER - 1;
    private final Map<Integer, Member> members = new LinkedHashMap<>();

    public MemberRegistry(Path dataDir) {
        this.fileName = dataDir.resolve("members.txt");
        load();
    }

    public Member findMember(int memberNumber) {
        return members.get(memberNumber);
    }

    public void addMember(Member member) {
        members.put(member.getID(), member);
        lastNumber = Math.max(lastNumber, member.getID());
        save();
    }

    public boolean removeMember(int memberNumber) {
        boolean removed = members.remove(memberNumber) != null;
        if (removed) {
            save();
        }
        return removed;
    }

    /** The number to give the next member. */
    public int nextMemberNumber() {
        return lastNumber + 1;
    }

    /** All members, sorted by member number. */
    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>(members.values());
        list.sort(Comparator.comparingInt(Member::getID));
        return list;
    }

    /** Writes every member to the data file. Called after every change. */
    public void save() {
        List<String> lines = new ArrayList<>();
        for (Member m : members.values()) {
            lines.add(RecordFile.join(m.getID(), m.getName(), m.getAddress(), m.getCity(), m.getState(), m.getZipCode(), m.getStatus()));

        }
        RecordFile.write(fileName, lines);
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            addSampleMembers();
            return;
        }
        for (String[] f : RecordFile.read(fileName, 7)) {
            Member m = new Member(Integer.parseInt(f[0]), f[1], f[2], f[3], f[4], f[5], MemberStatus.valueOf(f[6]));
            members.put(m.getID(), m);
            lastNumber = Math.max(lastNumber, m.getID());
        }
    }

    private void put(Member member) {
        members.put(member.getID(), member);
        lastNumber = Math.max(lastNumber, member.getID());
    }

    private void addSampleMembers() {
        put(new Member(100000001, "Ava Thompson", "412 Oak Street", "Tuscaloosa", "AL", "35401", MemberStatus.VALID));
        put(new Member(100000002, "Ben Carter", "88 River Road", "Northport", "AL", "35473", MemberStatus.VALID));
        put(new Member(100000003, "Chloe Nguyen", "1520 Hargrove Rd", "Tuscaloosa", "AL", "35401", MemberStatus.SUSPENDED));
        put(new Member(100000004, "Daniel Brooks", "905 Elm Avenue", "Birmingham", "AL", "35203", MemberStatus.VALID));
        save();
    }
}
