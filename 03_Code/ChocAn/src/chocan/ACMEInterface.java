package chocan;

import java.time.LocalDateTime;

/**
 * The connection to ACME Accounting Services, which collects membership
 * fees. ACME suspends members who owe fees and reinstates them once paid.
 * It changes member status only, through the MemberController.
 *
 * @author Gabriel Mckinney
 */
public class ACMEInterface {
    private String updateTime = "never";

    private final MemberController memberController;

    public ACMEInterface(MemberController memberController) {
        this.memberController = memberController;
    }

    /** Sets a member's status. Returns false if there is no such member. */
    public boolean updateMemberStatus(int memberNumber, MemberStatus status) {
        boolean updated = memberController.setStatus(memberNumber, status);
        if (updated) {
            updateTime = LocalDateTime.now().withNano(0).format(ServiceRecord.DATE_TIME);
        }
        return updated;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
