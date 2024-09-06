package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.utils.StringUtils;
import com.exe.sharkauction.models.OriginEntity;
import com.exe.sharkauction.repositories.IOriginRepository;
import com.exe.sharkauction.services.IOriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OriginService implements IOriginService {
    private final IOriginRepository originRepository;

    @Override
    public OriginEntity createOrigin(OriginEntity origin) {
        origin.setName(StringUtils.NameStandardlizing(origin.getName()));
        if(originRepository.existsByName(origin.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        return originRepository.save(origin);
    }

    @Override
    public OriginEntity getOriginById(long id) {
        return originRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This origin is not existed!"));
    }

    @Override
    public List<OriginEntity> getAllOrigins() {
        return originRepository.findAll();
    }

    @Override
    public OriginEntity updateOrigin(long originId, OriginEntity origin) {
        origin.setName(StringUtils.NameStandardlizing(origin.getName()));
        if(originRepository.existsByName(origin.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        OriginEntity existingOrigin = this.getOriginById(originId);
        existingOrigin.setName(origin.getName());
        return originRepository.save(existingOrigin);
    }

    @Override
    public void deleteOrigin(long id) {
        OriginEntity existingOrigin = getOriginById(id);
        originRepository.delete(existingOrigin);
    }
}
