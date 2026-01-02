package com.neworg.neworg.asset;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset findById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + id + " not found."));
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset createAsset(@Valid AssetDTO assetDTO) {
        if (assetRepository.existsByTickerIgnoreCase(assetDTO.ticker())) {
            throw new IllegalArgumentException("Asset with ticker " + assetDTO.ticker() + " already exists.");
        }
        Asset asset = new Asset(assetDTO.ticker(), assetDTO.name(), assetDTO.type());
        return assetRepository.save(asset);
    }

    public Asset editAsset(Long id, @Valid AssetDTO assetDTO) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + id + " not found."));
        if (!asset.getTicker().equalsIgnoreCase(assetDTO.ticker()) &&
            assetRepository.existsByTickerIgnoreCase(assetDTO.ticker())) {
            throw new IllegalArgumentException("Asset with ticker " + assetDTO.ticker() + " already exists.");
        }
        asset.setTicker(assetDTO.ticker());
        asset.setName(assetDTO.name());
        asset.setType(assetDTO.type());
        return assetRepository.save(asset);
    }

    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new IllegalArgumentException("Asset with id " + id + " not found.");
        }
        assetRepository.deleteById(id);
    }
}
