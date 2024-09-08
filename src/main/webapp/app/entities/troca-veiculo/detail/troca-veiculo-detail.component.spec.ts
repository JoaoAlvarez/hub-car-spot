import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrocaVeiculoDetailComponent } from './troca-veiculo-detail.component';

describe('TrocaVeiculo Management Detail Component', () => {
  let comp: TrocaVeiculoDetailComponent;
  let fixture: ComponentFixture<TrocaVeiculoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrocaVeiculoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./troca-veiculo-detail.component').then(m => m.TrocaVeiculoDetailComponent),
              resolve: { trocaVeiculo: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrocaVeiculoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrocaVeiculoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trocaVeiculo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrocaVeiculoDetailComponent);

      // THEN
      expect(instance.trocaVeiculo()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
