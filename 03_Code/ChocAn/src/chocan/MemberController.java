package chocan;

import java.util.List;

/**
 * Locgic for maintaining member records (Manage Records use case).
 * The OperatorTerminal and ACMEInterface go thorugh this class instead of 
 * changing the MemberRegistry directly
 * 
 * @author Aaron Suarez
 */
public class MemberController {
    private final MemberRegistry memberRegistry;

    public MemberController(MemberRegistry memberRegistry) {
        this.memberRegistry = memberRegistry;
    }

    /** Creates a new valid member with the next member number and returns it. */
    public Member addMember(String name, String street, String city, String state, String zip) {
        Member member = new Member(memberRegistry.nextMemberNumber(), name, street, city, state.toUpperCase(), zip, MemberStatus.VALID);
        memberRegistry.addMember(member);
        return member;
    }

    public boolean deleteMember(int memberNumber) {
        return memberRegistry.removeMember(memberNumber);
    }

    /** Replaces a member's name and address. Returns false if there is no such member. */
    public boolean updateMember(int memberNumber, String name, String street, String city, String state, String zipCode) {
        Member member = memberRegistry.findMember(memberNumber);
        if (member == null) {
            return false;
        }
        member.updateInfo(name, street, city, state.toUpperCase(), zipCode);
        memberRegistry.save();
        return true;
    }

    /** Suspends or reinstates a member. Returns false if there is no such member. */
    public boolean setStatus(int memberNumber, MemberStatus status) {
        Member member = memberRegistry.findMember(memberNumber);
        if (member == null) {
            return false;
        }
        if (status == MemberStatus.SUSPENDED) {
            member.suspend();
        } else {
            member.reinstate();
        }
        memberRegistry.save();
        return true;
    }

    public Member findMember(int memberNumber) {
        return memberRegistry.findMember(memberNumber);
    }

    public List<Member> getAllMembers() {
        return memberRegistry.getAllMembers();
    }
}
