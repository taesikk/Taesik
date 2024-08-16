package loginUnlock;

public interface UnlockService {
	public Unlock sendUnlockOTP(String otpVendor, String accountId, Unlock unlockVO);
}
