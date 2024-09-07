import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StatusDocumentoDetailComponent } from './status-documento-detail.component';

describe('StatusDocumento Management Detail Component', () => {
  let comp: StatusDocumentoDetailComponent;
  let fixture: ComponentFixture<StatusDocumentoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusDocumentoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./status-documento-detail.component').then(m => m.StatusDocumentoDetailComponent),
              resolve: { statusDocumento: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StatusDocumentoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusDocumentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load statusDocumento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StatusDocumentoDetailComponent);

      // THEN
      expect(instance.statusDocumento()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
