import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVendaVeiculo } from '../venda-veiculo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../venda-veiculo.test-samples';

import { RestVendaVeiculo, VendaVeiculoService } from './venda-veiculo.service';

const requireRestSample: RestVendaVeiculo = {
  ...sampleWithRequiredData,
  dataVenda: sampleWithRequiredData.dataVenda?.format(DATE_FORMAT),
};

describe('VendaVeiculo Service', () => {
  let service: VendaVeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: IVendaVeiculo | IVendaVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VendaVeiculoService);
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

    it('should create a VendaVeiculo', () => {
      const vendaVeiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vendaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VendaVeiculo', () => {
      const vendaVeiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vendaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VendaVeiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VendaVeiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VendaVeiculo', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVendaVeiculoToCollectionIfMissing', () => {
      it('should add a VendaVeiculo to an empty array', () => {
        const vendaVeiculo: IVendaVeiculo = sampleWithRequiredData;
        expectedResult = service.addVendaVeiculoToCollectionIfMissing([], vendaVeiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vendaVeiculo);
      });

      it('should not add a VendaVeiculo to an array that contains it', () => {
        const vendaVeiculo: IVendaVeiculo = sampleWithRequiredData;
        const vendaVeiculoCollection: IVendaVeiculo[] = [
          {
            ...vendaVeiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVendaVeiculoToCollectionIfMissing(vendaVeiculoCollection, vendaVeiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VendaVeiculo to an array that doesn't contain it", () => {
        const vendaVeiculo: IVendaVeiculo = sampleWithRequiredData;
        const vendaVeiculoCollection: IVendaVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addVendaVeiculoToCollectionIfMissing(vendaVeiculoCollection, vendaVeiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vendaVeiculo);
      });

      it('should add only unique VendaVeiculo to an array', () => {
        const vendaVeiculoArray: IVendaVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vendaVeiculoCollection: IVendaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addVendaVeiculoToCollectionIfMissing(vendaVeiculoCollection, ...vendaVeiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vendaVeiculo: IVendaVeiculo = sampleWithRequiredData;
        const vendaVeiculo2: IVendaVeiculo = sampleWithPartialData;
        expectedResult = service.addVendaVeiculoToCollectionIfMissing([], vendaVeiculo, vendaVeiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vendaVeiculo);
        expect(expectedResult).toContain(vendaVeiculo2);
      });

      it('should accept null and undefined values', () => {
        const vendaVeiculo: IVendaVeiculo = sampleWithRequiredData;
        expectedResult = service.addVendaVeiculoToCollectionIfMissing([], null, vendaVeiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vendaVeiculo);
      });

      it('should return initial array if no VendaVeiculo is added', () => {
        const vendaVeiculoCollection: IVendaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addVendaVeiculoToCollectionIfMissing(vendaVeiculoCollection, undefined, null);
        expect(expectedResult).toEqual(vendaVeiculoCollection);
      });
    });

    describe('compareVendaVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVendaVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareVendaVeiculo(entity1, entity2);
        const compareResult2 = service.compareVendaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareVendaVeiculo(entity1, entity2);
        const compareResult2 = service.compareVendaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareVendaVeiculo(entity1, entity2);
        const compareResult2 = service.compareVendaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
