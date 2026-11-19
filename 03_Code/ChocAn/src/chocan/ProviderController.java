package ChocAn.src.chocan;

import java.util.List;

/** 
 * Logic for maintaining provider records (Manage Records use case).
 * The OperatorTerminal goes through this class instead of changing the
 * ProviderRegistry directly.
 * 
 * @author Aaron Suarez
 */

public class ProviderController {
    private final ProviderRegistry providerRegistry;

    public ProviderController(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    /** Creates a new provider with the next provider number and returns it. */
    public Provider addProvider(String name, String street, String city, String state, String zipCode) {
        Provider provider = new Provider(providerRegistry.nextProviderNumber(), name, street, city,
                state.toUpperCase(), zipCode);
        providerRegistry.addProvider(provider);
        return provider;
    }

    public boolean deleteProvider(int providerNumber) {
        return providerRegistry.removeProvider(providerNumber);
    }

    /** Replaces a provider's name and address. Returns false if there is no such provider. */
    public boolean updateProvider(int providerNumber, String name, String street, String city,
                                  String state, String zipCode) {
        Provider provider = providerRegistry.findProvider(providerNumber);
        if (provider == null) {
            return false;
        }
        provider.setName(name);
        provider.setAddress(street);
        provider.setCity(city);
        provider.setState(state.toUpperCase());
        provider.setZipCode(zipCode);
        providerRegistry.save();
        return true;
    }

    public Provider findProvider(int providerNumber) {
        return providerRegistry.findProvider(providerNumber);
    }

    public List<Provider> getAllProviders() {
        return providerRegistry.getAllProviders();
    }
}
