import { useEffect, useState } from 'react';
import Step1 from './components/Step1';
import Step2 from './components/Step2';
import Step3 from './components/Step3';
import Step4 from './components/Step4';

function App() {
  const [cartId, setCartId] = useState(null);
  const [step, setStep] = useState(1);
  const [order, setOrder] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/cart?customerIdentifier=web-musteri-' + Date.now(), {
      method: 'POST',
    })
        .then((res) => res.json())
        .then((data) => setCartId(data.id));
  }, []);

  if (!cartId) {
    return <p>Sepet oluşturuluyor...</p>;
  }

  return (
      <div style={{ maxWidth: 700, margin: '40px auto', fontFamily: 'sans-serif' }}>
        <h1>Hat Aktivasyon Başvurusu</h1>
        {step === 1 && <Step1 cartId={cartId} onNext={() => setStep(2)} />}
        {step === 2 && <Step2 cartId={cartId} onNext={() => setStep(3)} onBack={() => setStep(1)} />}
        {step === 3 && (
            <Step3
                cartId={cartId}
                onBack={() => setStep(2)}
                onOrderCreated={(createdOrder) => {
                  setOrder(createdOrder);
                  setStep(4);
                }}
            />
        )}
        {step === 4 && <Step4 order={order} />}
      </div>
  );
}

export default App;