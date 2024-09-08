import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVeiculo } from '../veiculo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../veiculo.test-samples';

import { RestVeiculo, VeiculoService } from './veiculo.service';

const requireRestSample: RestVeiculo = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
};

describe('Veiculo Service', () => {
  let service: VeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: IVeiculo | IVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VeiculoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Veiculo', () => {
      const veiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(veiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Veiculo', () => {
      const veiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(veiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Veiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Veiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Veiculo', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVeiculoToCollectionIfMissing', () => {
      it('should add a Veiculo to an empty array', () => {
        const veiculo: IVeiculo = sampleWithRequiredData;
        expectedResult = service.addVeiculoToCollectionIfMissing([], veiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(veiculo);
      });

      it('should not add a Veiculo to an array that contains it', () => {
        const veiculo: IVeiculo = sampleWithRequiredData;
        const veiculoCollection: IVeiculo[] = [
          {
            ...veiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, veiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Veiculo to an array that doesn't contain it", () => {
        const veiculo: IVeiculo = sampleWithRequiredData;
        const veiculoCollection: IVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, veiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(veiculo);
      });

      it('should add only unique Veiculo to an array', () => {
        const veiculoArray: IVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const veiculoCollection: IVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, ...veiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const veiculo: IVeiculo = sampleWithRequiredData;
        const veiculo2: IVeiculo = sampleWithPartialData;
        expectedResult = service.addVeiculoToCollectionIfMissing([], veiculo, veiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(veiculo);
        expect(expectedResult).toContain(veiculo2);
      });

      it('should accept null and undefined values', () => {
        const veiculo: IVeiculo = sampleWithRequiredData;
        expectedResult = service.addVeiculoToCollectionIfMissing([], null, veiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(veiculo);
      });

      it('should return initial array if no Veiculo is added', () => {
        const veiculoCollection: IVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, undefined, null);
        expect(expectedResult).toEqual(veiculoCollection);
      });
    });

    describe('compareVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareVeiculo(entity1, entity2);
        const compareResult2 = service.compareVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareVeiculo(entity1, entity2);
        const compareResult2 = service.compareVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareVeiculo(entity1, entity2);
        const compareResult2 = service.compareVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
