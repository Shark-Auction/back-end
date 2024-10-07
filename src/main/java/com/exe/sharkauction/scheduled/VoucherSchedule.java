package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.models.VoucherEntity;
import com.exe.sharkauction.models.enums.VoucherStatus;
import com.exe.sharkauction.repositories.IVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VoucherSchedule {

    @Autowired
    private IVoucherRepository voucherRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateVoucherStatus() {
        Date currentDate = new Date();

        List<VoucherEntity> vouchers = voucherRepository.findVouchersToUpdate(currentDate);

        for (VoucherEntity voucher : vouchers) {
            if (voucher.getStatus() != VoucherStatus.Used) {
                if (currentDate.before(voucher.getStartTime())) {
                    voucher.setStatus(VoucherStatus.Pending);
                } else if (currentDate.after(voucher.getEndTime())) {
                    voucher.setStatus(VoucherStatus.Expired);
                } else {
                    voucher.setStatus(VoucherStatus.Available);
                }

                voucherRepository.save(voucher);
            }
        }
    }
}
