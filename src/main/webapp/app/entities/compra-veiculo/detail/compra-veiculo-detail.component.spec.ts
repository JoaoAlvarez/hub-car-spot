import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompraVeiculoDetailComponent } from './compra-veiculo-detail.component';

describe('CompraVeiculo Management Detail Component', () => {
  let comp: CompraVeiculoDetailComponent;
  let fixture: ComponentFixture<CompraVeiculoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompraVeiculoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./compra-veiculo-detail.component').then(m => m.CompraVeiculoDetailComponent),
              resolve: { compraVeiculo: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CompraVeiculoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompraVeiculoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compraVeiculo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompraVeiculoDetailComponent);

      // THEN
      expect(instance.compraVeiculo()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
