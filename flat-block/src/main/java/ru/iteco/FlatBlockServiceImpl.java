package ru.iteco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FlatBlockServiceImpl implements FlatBlockService {

    private final String HASH_KEY = "application:flatBlock";

    private final FlatBlockRepository flatBlockRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public FlatBlockServiceImpl(FlatBlockRepository flatBlockRepository, RedisTemplate<String, String> redisTemplate) {
        this.flatBlockRepository = flatBlockRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getContentByName(String name) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(HASH_KEY);
        String content = hashOps.get(name);
        if (content == null) {
            FlatBlock flatBlock = flatBlockRepository.findOneByName(name);
            if (flatBlock != null) {
                String key = flatBlock.getName();
                hashOps.put(key, flatBlock.getContent());
                content = flatBlock.getContent();
            }
        }
        return content == null ? String.format("FlatBlock %s can't find", name) : content;
    }

    @Override
    public void update(FlatBlock flatBlock) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(HASH_KEY);
        String key = flatBlock.getName();
        hashOps.delete(key);
        flatBlockRepository.save(flatBlock);
    }
}
